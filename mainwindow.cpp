#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    ui->label_pico_status->setText("Emstat Pico not connected");
    ui->pushButton_connect->setEnabled(false);
    ui->pushButton_disconnect->setEnabled(false);
    ui->pushButton_run->setEnabled(false);
    ui->pushButton_test->setEnabled(false);
    ui->pushButton_analyze->setEnabled(false);

    //Initialize serial Facade
    mySerialFacade = new SerialFacade();
    // Initialize Api
    myNetworkApi = new ApiFacade();
    // Initialize Database Facade
    myDbFacade = new DbFacade();

    // Initialize Analysis list
    for (int i = 0; i < listOfAnalysis.size(); i++)
    {
        ui->comboBox_technique->addItem(listOfAnalysis.at(i));
    }

    // Init Queues to stores values of potential and current

    connect(mySerialFacade, &SerialFacade::on_pico_status_received, this, &MainWindow::pico_status_received);
    connect(mySerialFacade, &SerialFacade::jsonline_received, this, &MainWindow::jsonline_received);
    connect(myNetworkApi, &ApiFacade::on_network_response, this, &MainWindow::handle_api_response);
    connect(mySerialFacade, &SerialFacade::fileNotSavedError, this, &MainWindow::fileNotSaved);
    connect(myDbFacade, &DbFacade::db_error, this, &MainWindow::db_error);

    init_chart();
}

MainWindow::~MainWindow()
{
    delete ui;
}


void MainWindow::on_pushButton_ListPorts_clicked()
{
    QList<QString> serialPorts = mySerialFacade->getAllPortsName();
    int numberOfPorts = serialPorts.size();
    ui->label_3->setText(QString::number(numberOfPorts));

    if (numberOfPorts > 0){
        ui->pushButton_connect->setEnabled(true);
        ui->pushButton_disconnect->setEnabled(false);

        for (int i = 0; i < serialPorts.size(); i++)
        {
            ui->comboBox_ports->addItem(serialPorts.at(i));
        }
    }

}


void MainWindow::on_pushButton_connect_clicked()
{
    QString portName =  ui->comboBox_ports->currentText();
    if (mySerialFacade->openPort(portName)) {
        ui->pushButton_test->setEnabled(true);
        ui->pushButton_connect->setEnabled(false);
        ui->pushButton_disconnect->setEnabled(true);
    }
}


void MainWindow::on_pushButton_disconnect_clicked()
{
    mySerialFacade->closePort();
    ui->pushButton_test->setEnabled(false);
    ui->pushButton_connect->setEnabled(true);
    ui->pushButton_disconnect->setEnabled(false);
}


void MainWindow::on_pushButton_run_clicked()
{
    mySerialFacade->sendVoltametryRequest();
    series->clear();
    max_current = 0.0000001 * 1000000;
    min_current = -0.0000001 * 1000000;
    axisY->setRange(min_current, max_current);

}


void MainWindow::on_pushButton_test_clicked()
{
    mySerialFacade->sendTestConnectionRequest();
}

void MainWindow::pico_status_received(Protocol::STATUS status)
{
    switch (status) {
        case Protocol::Emstat_OK:
        ui->label_pico_status->setText("Emstat pico ready");
        ui->pushButton_run->setEnabled(true);
        break;

        case Protocol::Emstat_NOK:
        ui->label_pico_status->setText("Emstat pico not ready");
        ui->pushButton_run->setEnabled(false);
        break;

        case Protocol::Begin_Measurement:
        ui->pushButton_run->setEnabled(false);
        ui->label_meas_status->setText("Measuring...");

        potQueue = new QQueue<double>();
        currQueue = new QQueue<double>();
        potQueue->clear();
        currQueue->clear();

        timer = new QTimer();
        connect(timer, &QTimer::timeout, this, &MainWindow::handle_chart_update);


        break;

        case Protocol::Finished_Measurement:
        ui->pushButton_run->setEnabled(true);
        ui->label_meas_status->setText("End Measuring!");
        ui->pushButton_analyze->setEnabled(true);
        // API logic here
        QString filepath = ui->lineEdit_path->text();

        if (filepath.length() > 0)
        {
            mySerialFacade->saveFile(filepath.toUtf8().constData());
        }

        break;
    }
}

// Used to plot data in real time
void MainWindow::jsonline_received(QString line)
{
     //qDebug() << line;
     QStringList numbers = line.split(",");
     double potential = numbers[0].toDouble();
     double current = numbers[1].toDouble();
     currQueue->enqueue(current);
     potQueue->enqueue(potential);
     plot = true;
     timer->start(20);
}

void MainWindow::handle_chart_update()
{
    int i = 0;
    double current, potential;
    if (plot)
    {
        if (!currQueue->isEmpty())
        {
             current = currQueue->dequeue() * 1000000;
             if (max_current < current)
             {
                 max_current =  current;
             }

             if (min_current > current){
                 min_current = current;
             }

             qDebug() << "Current: " << current;
             i++;

        }

        if (!potQueue->isEmpty()){
            potential = potQueue->dequeue();
            qDebug() << "Potential: " << potential;
            i++;
        }

        if (i == 2)
        {
            time_passed = 0;
            // Update series
            axisY->setRange(min_current, max_current);
            series->append(potential, current);

            // Ensure the chart updates

//            chart->scroll(chart->plotArea().width() / 10, 0);

        }else {
            plot = false;
        }

    }else {
        if (time_passed == 100){
             timer->stop();
             int sum = 0;

             while(!potQueue->isEmpty()){
                 sum++;
                 potQueue->dequeue();
             }

             qDebug() << "Time Finished" << sum;
             sum = 0;

             while(!currQueue->isEmpty()){
                 sum++;
                     currQueue->dequeue();
             }

             qDebug() << "Time Finished" << sum;
             disconnect(timer, &QTimer::timeout, this, &MainWindow::handle_chart_update);
        }else{
            time_passed++;
        }
    }
}

void MainWindow::handle_api_response(QString response)
{
    qDebug() << "Main Window" << response;
    if (response.contains("Error") || response.contains("time"))
    {
        ui->label_status_api->setText("Conection Error");
    }else{
        ui->label_result->setText(response);
        ui->pushButton_analyze->setEnabled(false);
    }

}

void MainWindow::init_chart()
{
    // Create a line series for the graph
    series = new QtCharts::QLineSeries();

    // Create a chart and attach the series
    chart = new QtCharts::QChart();
    chart->addSeries(series);
    chart->legend()->hide();

    // Create and set up the X-axis and Y-axis
    axisX = new QtCharts::QValueAxis();
    axisY = new QtCharts::QValueAxis();
    axisX->setTitleText("Potential (V)");
    axisY->setTitleText("Current (uA)");
    axisX->setRange(-1.0, 1.0);
    chart->addAxis(axisX, Qt::AlignBottom);
    chart->addAxis(axisY, Qt::AlignLeft);
    series->attachAxis(axisX);
    series->attachAxis(axisY);


    chartView = new QtCharts::QChartView(chart);
    chartView->setRenderHint(QPainter::Antialiasing);

    QVBoxLayout *graph_layout = ui->layout_chart;
    if (graph_layout != nullptr)
    {
        graph_layout->addWidget(chartView);
    }else{
        qDebug() << "Layout not encountered";
    }

//    this->resize(400, 300);
//    this->show();
}


void MainWindow::on_pushButton_analyze_clicked()
{

    QByteArray byte_measures = mySerialFacade->getMeasurements();
    mySerialFacade->saveJsonFile(ui->lineEdit_path->text().toUtf8().constData());
    myNetworkApi->sendMeasurements(byte_measures);
}


void MainWindow::on_pushButton_clicked()
{
    // Chose the path to save the csv files
    QString filePath = QFileDialog::getExistingDirectory(this, "Open File");
    if (!filePath.isEmpty()){
        ui->lineEdit_path->setText(filePath);
    }
}

void MainWindow::fileNotSaved()
{
    ui->label_meas_status->setText("File Not saved");
}

void MainWindow::db_error(const QString &error_message)
{
     ui->label_meas_status->setText("Database Error " + error_message);
}

