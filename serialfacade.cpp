#include "serialfacade.h"

SerialFacade::SerialFacade(QObject *parent)
    : QObject{parent}
{
    myConnection = new SerialConnection();
    myReader = new SerialPortReader(this);
    myWriter = new SerialPortWriter(this);
    myPortsManager = new SerialManager();
    jsonConverter = new JsonToCsvConverter();
}

// Get the number of serial ports
qint64 SerialFacade::getTotalOfPorts()
{
    return myPortsManager->serialCount();
}

QList<QString> SerialFacade::getAllPortsName()
{
    return myPortsManager->getPortsNames();
}

bool SerialFacade::openPort(QString portName)
{
    //If portName is empty
    if (portName.isEmpty()) {
        return false;
    }

    if (! myConnection->initSerialPort(portName)) {
        return false;
    }

    // Opens serial port
    if (! myConnection->openPort()) {
        return false;
    }

    QSerialPort *serialPort = myConnection->getSerialPort();
    serialPort->setDataTerminalReady(true);

    myWriter->setSerialPort(serialPort);
    myReader->setSerialPort(serialPort);

    // Configure connections between serialReader and Serial Writer
    setupConnections();

    return true;
}

void SerialFacade::closePort()
{
    myConnection->closePort();
}

bool SerialFacade::sendVoltametryRequest()
{
    if (myConnection->getPortStatus()) {
        myWriter->write(protocol.CYCLIC);
        return true;
    }
    return false;
}

bool SerialFacade::sendPulseDifferentialRequest()
{
    {
        if (myConnection->getPortStatus()) {
//            myWriter->write(protocol.getPacket(Dpv, params));
            return true;
        }
        return false;
    }
}

// Sends a test connection request
bool SerialFacade::sendTestConnectionRequest()
{
    if (myConnection->getPortStatus()) {
        myWriter->write(protocol.CMD_VERSION_STRING);
        return true;
    }
    return false;
}

bool SerialFacade::sendChronoAmperometryRequest()
{
    if (myConnection->getPortStatus()) {
        //myWriter->write(protocol.getPacket(ChronoAmperometry, params));
        return true;
    }
    return false;
}

void SerialFacade::setupConnections()
{
    connect(myReader, &SerialPortReader::handleDataReceived, this, &SerialFacade::handleDataReceived);
    connect(myWriter, &SerialPortWriter::bytesSentStatus, this, &SerialFacade::handleDataWritten);
}

void SerialFacade::handleDataReceived(QString data)
{
//    qDebug() << "Serial Facade: " << data;
    QStringList packetList = data.split('\n');

    for (int i = 0; i < packetList.size(); i++)
    {
        QString s = packetList.at(i);
        if ("Pico_OK" == s){
            emit on_pico_status_received(true);
        }
        else if ("Receiving_measurement" == s)
        {
            this->status = this->status + 1;
        }
        else if ("End_Measurement" == s){
            this->status = 0;
            jsonConverter->writecsv();
             // Finaliza a medição e também salva o arquivo
         }
        else if ("Pico_NOK" == s){
           emit on_pico_status_received(false);
        }

        if (s.contains("{") && s.contains("}") && this->status == 2){
            //Json Packet
            QString csvLine = jsonConverter->convertJsonToCSV(s);
            emit on_jsonline_received(csvLine);
//            qDebug() << csvLine;

            if (csvLine.length() > 2)
            {
              jsonConverter->addLine(csvLine);
            }

        }
    }
}

void SerialFacade::handleDataWritten(QSerialPort::SerialPortError error)
{
    if (error == QSerialPort::SerialPortError::NoError) {
        qDebug() << "Serial Facade: " << "Data sent successifuly";
    }else {
        qDebug() << "Error: " << QString::number(error);
    }
}
