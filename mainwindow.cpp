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

    //Initialize serial Facade
    mySerialFacade = new SerialFacade();

    // Initialize Analysis list
    for (int i = 0; i < listOfAnalysis.size(); i++)
    {
        ui->comboBox_technique->addItem(listOfAnalysis.at(i));
    }

    connect(mySerialFacade, &SerialFacade::on_pico_status_received, this, &MainWindow::on_pico_status_received);
    connect(mySerialFacade, &SerialFacade::on_jsonline_received, this, &MainWindow::on_jsonline_received);
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
}


void MainWindow::on_pushButton_test_clicked()
{
    mySerialFacade->sendTestConnectionRequest();
}

void MainWindow::on_pico_status_received(bool status)
{
    if (status)
    {
        ui->label_pico_status->setText("Emstat pico ready");
        ui->pushButton_run->setEnabled(true);
    }else{
        ui->label_pico_status->setText("Emstat pico not ready");
        ui->pushButton_run->setEnabled(false);
    }
}

void MainWindow::on_jsonline_received(QString line)
{
     qDebug() << line;
     QStringList numbers = line.split(",");
     double potential = numbers[0].toDouble();
     double current = numbers[1].toDouble();
     qDebug() << "Potential: " << potential;
     qDebug() << "Current: " << current;
}

void MainWindow::on_handle_chart_update()
{

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
    chart->addAxis(axisX, Qt::AlignBottom);
    chart->addAxis(axisY, Qt::AlignLeft);

    chartView = new QtCharts::QChartView(chart);
    chartView->setRenderHint(QPainter::Antialiasing);

    this->setCentralWidget(chartView);
    timer = new QTimer();

    connect(timer, &QTimer::timeout, this, &MainWindow::on_handle_chart_update);
}

