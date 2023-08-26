#ifndef SERIALPORTREADER_H
#define SERIALPORTREADER_H

#include <QObject>
#include <QtSerialPort/QSerialPort>
#include <QTimer>
#include <QByteArray>
#include <QDebug>


class SerialPortReader : public QObject
{

    Q_OBJECT

public:
    SerialPortReader(QObject *parent);
    void setSerialPort(QSerialPort*);

signals:
    // When the data if
    void handleDataReceived(QString data);

private slots:
    void handleReadyRead();
    void handleTimeout();
    void handleError(QSerialPort::SerialPortError error);
    QString byteArrayToString(const QByteArray &dataArray);

private:
    QSerialPort *serialPort = nullptr;
    QByteArray readData;
    QTimer timer;
    bool portInitialized = false;

};

#endif // SERIALPORTREADER_H
