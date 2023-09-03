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
    QByteArray getMeasurements();
    void clearMeasurements();
    void saveFile(const char *filePath);
    void saveJsonFile(const char *filePath);

signals:
    void on_pico_status_received(Protocol::STATUS status);
    void jsonline_received(QString line);
    void fileNotSavedError(void);

private:
    SerialPortReader *myReader = nullptr;
    SerialPortWriter *myWriter = nullptr;
    SerialConnection *myConnection = nullptr;
    SerialManager *myPortsManager = nullptr;
    JsonToCsvConverter *jsonConverter = nullptr;
    Protocol protocol;
    int status = 0;

    void setupConnections();

private slots:

    void handleDataReceived(QString data);
    void handleDataWritten(QSerialPort::SerialPortError error);
    void handleFileNotSaved();
};

#endif // SERIALFACADE_H
