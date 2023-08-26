#ifndef SERIALCONNECTION_H
#define SERIALCONNECTION_H

#include <QObject>
#include <QtSerialPort/QSerialPort>
#include <QtSerialPort/QSerialPortInfo>

class SerialConnection : public QObject
{
    Q_OBJECT
public:
    explicit SerialConnection(QObject *parent = nullptr);
    ~SerialConnection(void);
    bool initSerialPort(QString portName);
    bool openPort(void);
    void closePort(void);
    bool getPortStatus(void);
    QSerialPort *getSerialPort();

private:
    QSerialPort *mySerial;
    const qint32 baudRate = 230400;
    const qint64 bufferSize = 500;

signals:
    void errorConnection(QSerialPort::SerialPortError error);

private slots:
    void errorHandler(QSerialPort::SerialPortError error);
};

#endif // SERIALCONNECTION_H
