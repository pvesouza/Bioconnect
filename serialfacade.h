#ifndef SERIALFACADE_H
#define SERIALFACADE_H

#include <QObject>
#include "serialconnection.h"
#include "serialportreader.h"
#include "serialportwriter.h"
#include "serialmanager.h"
#include "protocol.h"
#include "jsontocsvconverter.h"


class SerialFacade : public QObject
{
    Q_OBJECT
public:
    explicit SerialFacade(QObject *parent = nullptr);
    qint64 getTotalOfPorts();
    QList<QString> getAllPortsName();
    bool openPort(QString portName);
    void closePort();
    bool sendVoltametryRequest();
    bool sendPulseDifferentialRequest();
    bool sendTestConnectionRequest();
    bool sendChronoAmperometryRequest();

signals:
    void on_pico_status_received(bool status);
    void on_jsonline_received(QString line);

private:
    SerialPortReader *myReader = nullptr;
    SerialPortWriter *myWriter = nullptr;
    SerialConnection *myConnection = nullptr;
    SerialManager *myPortsManager = nullptr;
    Protocol protocol;
    JsonToCsvConverter *jsonConverter = nullptr;
    int status = 0;

    void setupConnections();

private slots:

    void handleDataReceived(QString data);
    void handleDataWritten(QSerialPort::SerialPortError error);
};

#endif // SERIALFACADE_H
