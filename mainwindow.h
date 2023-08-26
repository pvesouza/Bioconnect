#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QString>
#include <QDebug>
#include <QLabel>
#include <QtCharts/QChartView>
#include <QtCharts/QLineSeries>
#include <QtCharts/QValueAxis>

#include "serialfacade.h"
#include "serialmanager.h"


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

    void on_pico_status_received(bool status);

    void on_jsonline_received(QString line);

    void on_handle_chart_update();



private:
    Ui::MainWindow *ui;
    SerialFacade *mySerialFacade = nullptr;
    QStringList listOfAnalysis = {
      "Analysis 1",
       "Analysis 2",
        "Analysis 3"
    };

    // Graphical elements
    QtCharts::QChart *chart = nullptr;
    QtCharts::QLineSeries *series = nullptr;
    QtCharts::QChartView *chartView = nullptr;
    QtCharts::QValueAxis *axisX = nullptr;
    QtCharts::QValueAxis *axisY = nullptr;
    QTimer *timer = nullptr;

    void init_chart();

};
#endif // MAINWINDOW_H
