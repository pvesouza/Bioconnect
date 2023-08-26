#ifndef SERIALPORTWRITER_H
#define SERIALPORTWRITER_H

#include <QObject>
#include <QByteArray>
#include <QtSerialPort/QSerialPort>
#include <QDebug>
#include <QString>
#include <QTimer>

class SerialPortWriter : public QObject
{
    Q_OBJECT

public:
    explicit SerialPortWriter(QSerialPort *serialPort, QObject *parent = nullptr);
    explicit SerialPortWriter(QObject *parent = nullptr);
    void write(const QByteArray &data);
//    void write_data(const char *data);
    void setSerialPort(QSerialPort *serialPort);

signals:
    void bytesSentStatus(QSerialPort::SerialPortError error);

private slots:
    void handleBytesWritten(qint64 bytes);
    void handleTimeout();
    void handleError(QSerialPort::SerialPortError error);

private:
    QSerialPort *mySerialPort;
    QByteArray writeData;
    qint64 bytesWritten = 0;
    QTimer myTimer;
    const long TIMEOUT = 5000l;

    void connectSlotToSignals();

};

#endif // SERIALPORTWRITER_H
