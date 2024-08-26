#include "serialportwriter.h"

SerialPortWriter::SerialPortWriter(QSerialPort *serialPort, QObject *parent):
    QObject(parent),
    mySerialPort(serialPort)
{
    myTimer.setSingleShot(true);
    connectSlotToSignals();
}

SerialPortWriter::SerialPortWriter(QObject *parent):
    QObject(parent)
{
    myTimer.setSingleShot(true);
}

// Writes a byte array in serial port
void SerialPortWriter::write(const QByteArray &data)
{
    writeData.clear();
    writeData = data;

    const qint64 bytesWritten = mySerialPort->write(writeData);
//    qDebug() << "Result sent: " << QString::number(bytesWritten);

    // Tests if an error has occured
    if (bytesWritten == -1) {
        qDebug() << "I/O error";
    }
    else if (bytesWritten != writeData.size()) {
        qDebug() << "Fail to write all data";
    }

    myTimer.start(TIMEOUT);
}

//void SerialPortWriter::write_data(const char *data)
//{
//    int lenght = 0;
//    char c = '\0';
//    writeData.clear();
//    // Calculates the lenght of data
//    do {
//        c = data[lenght];
//        qDebug() << "data: " + QString(c);
//        lenght++;
//    }while (c != '\0');

//    qDebug() << "lenght: " + QString::number(lenght);

//    // Saves it to a QByteArray
//    writeData = QByteArray(data, lenght - 1);
//    // Writes it to serial port
//    write(writeData);
//}

void SerialPortWriter::setSerialPort(QSerialPort *serialPort)
{
    mySerialPort = serialPort;
    connectSlotToSignals();
}

// Handles the bytes written through serial port
void SerialPortWriter::handleBytesWritten(qint64 bytes)
{
    bytesWritten = bytesWritten + bytes;
    // Tests if all bytes have been sent
    if (bytesWritten == writeData.size()) {
        bytesWritten = 0;
        myTimer.stop();

        emit bytesSentStatus(QSerialPort::SerialPortError::NoError);

//        qDebug() << "Data was sent successifully\n";
    }
}

// Handles the time out of sent data
void SerialPortWriter::handleTimeout()
{
    qDebug() << "Timeout!!!";
}

// Handles the serial port write error
void SerialPortWriter::handleError(QSerialPort::SerialPortError error)
{
    if (error == QSerialPort::SerialPortError::WriteError) {
        qDebug() << "Error to write data in " + mySerialPort->portName() + "\n";
        emit bytesSentStatus(error);
    }
}

 // Connects slots to signals
void SerialPortWriter::connectSlotToSignals()
{
    connect(mySerialPort, &QSerialPort::bytesWritten, this, &SerialPortWriter::handleBytesWritten);
    connect(mySerialPort, &QSerialPort::errorOccurred, this, &SerialPortWriter::handleError);
    connect(&myTimer, &QTimer::timeout, this, &SerialPortWriter::handleTimeout);
}
