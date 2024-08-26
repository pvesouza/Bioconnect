#include "serialconnection.h"

SerialConnection::SerialConnection(QObject *parent)
    : QObject{parent}
{
    // Constructs a Serial Port Object
    this->mySerial = new QSerialPort();
    connect(mySerial, &QSerialPort::errorOccurred, this, &SerialConnection::errorHandler);
}

SerialConnection::~SerialConnection()
{
    delete this->mySerial;
}

bool SerialConnection::initSerialPort(QString portName)
{
    // Configures the serial port to communicate with EMStat Pico core
    this->mySerial->setPortName(portName);
    this->mySerial->setBaudRate(baudRate);                                        // Baudrate = 230400
    bool b1 = this->mySerial->setDataBits(QSerialPort::Data8);                    // 8 data bits
    bool b2 = this->mySerial->setParity(QSerialPort::NoParity);                   // No Parity
    bool b3 = this->mySerial->setStopBits(QSerialPort::OneStop);                  // One stop bit
    bool b4 = this->mySerial->setFlowControl(QSerialPort::NoFlowControl);         // No flow control
    this->mySerial->setReadBufferSize(bufferSize);                                // Reading buffer with 500 byte

    return (b1 & b2 & b3 & b4);
}

bool SerialConnection::openPort()
{
    bool result = false;
    if (!getPortStatus()) {
       result = this->mySerial->open(QIODevice::ReadWrite);
    }
    return result;
}
// Closes serial port communication
void SerialConnection::closePort()
{
    if (mySerial->isOpen()) {
        mySerial->close();
    }
}

bool SerialConnection::getPortStatus()
{
    return mySerial->isOpen();
}

QSerialPort *SerialConnection::getSerialPort()
{
    return mySerial;
}

void SerialConnection::errorHandler(QSerialPort::SerialPortError error)
{
    emit errorConnection(error);
}

