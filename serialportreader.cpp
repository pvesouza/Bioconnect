#include "serialportreader.h"

SerialPortReader::SerialPortReader(QObject *parent) :
    QObject(parent)
{

}

void SerialPortReader::setSerialPort(QSerialPort *serialPort)
{
    if (!portInitialized) {
        qDebug() << "Serial Reader initialized";
         this->serialPort = serialPort;
        portInitialized = true;
        connect(serialPort, &QSerialPort::readyRead, this, &SerialPortReader::handleReadyRead);
        connect(serialPort, &QSerialPort::errorOccurred, this, &SerialPortReader::handleError);
        connect(&timer, &QTimer::timeout, this, &SerialPortReader::handleTimeout);
    }

}

// Handles the serial port reading
void SerialPortReader::handleReadyRead()
{
    QByteArray dataRead = serialPort->readAll();

    if (dataRead.size() != 0) {
        readData.append(dataRead);
        if (!timer.isActive()) {
            timer.start(20);
        }
    }
}

// Handles timeout serial
void SerialPortReader::handleTimeout()
{
    if (readData.isEmpty()) {
        qDebug() << "No data currently available";
    }
    else{

        emit handleDataReceived(byteArrayToString(readData));
        readData.clear();
    }
    timer.stop();
}

// Handles the serial port reading error
void SerialPortReader::handleError(QSerialPort::SerialPortError error)
{
    if (error == QSerialPort::SerialPortError::ReadError) {
        qDebug() << "Error reading serial port";
    }
}

QString SerialPortReader::byteArrayToString(const QByteArray &dataArray)
{
    QString str = nullptr;
    for (int i = 0; i < dataArray.size(); i++) {
//        qDebug() << (char) dataArray.at(i);
        str.append(dataArray.at(i));
    }
    return str;
}
