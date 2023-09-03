#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QString>
#include <QDebug>
#include <QLabel>
#include <QQueue>
#include <QtCharts/QChartView>
#include <QtCharts/QLineSeries>
#include <QtCharts/QValueAxis>
#include <QFileDialog>

#include "serialfacade.h"
#include "serialmanager.h"
#include "apifacade.h"


QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void on_pushButton_ListPorts_clicked();

    void on_pushButton_connect_clicked();

    void on_pushButton_disconnect_clicked();

    void on_pushButton_run_clicked();

    void on_pushButton_test_clicked();

    void pico_status_received(Protocol::STATUS status);

    void jsonline_received(QString line);

    void handle_chart_update();

    void handle_api_response(QString response);

    void on_pushButton_analyze_clicked();

    void on_pushButton_clicked();
    void fileNotSaved(void);

private:
    Ui::MainWindow *ui;
    SerialFacade *mySerialFacade = nullptr;
    ApiFacade *myNetworkApi = nullptr;
    int time_passed = 0;

    QStringList listOfAnalysis = {
      "Analysis 1",
       "Analysis 2",
        "Analysis 3"
    };
    double max_current;
    double min_current;

    // Graphical elements
    QtCharts::QChart *chart = nullptr;
    QtCharts::QLineSeries *series = nullptr;
    QtCharts::QChartView *chartView = nullptr;
    QtCharts::QValueAxis *axisX = nullptr;
    QtCharts::QValueAxis *axisY = nullptr;
    QTimer *timer = nullptr;
    QQueue<double> *potQueue = nullptr;
    QQueue<double> *currQueue = nullptr;


    bool plot = false;
    void init_chart();

};
#endif // MAINWINDOW_H
