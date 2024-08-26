#include "serialmanager.h"

SerialManager::SerialManager()
{
     portsNames = QStringList();
}

SerialManager::~SerialManager()
{

}

int SerialManager::serialCount()
{
    return QSerialPortInfo::availablePorts().length();
}

QList<QSerialPortInfo> SerialManager::getAll()
{
    return QSerialPortInfo::availablePorts();
}

QString SerialManager::getAllPortsInfo()
{
    const QList<QSerialPortInfo> list = getAll();
    QString s;

    for (const QSerialPortInfo &info : list) {

        //  Saves all ports names
        portsNames.append(info.portName());

         QString s1 = QObject::tr("Port: ") + info.portName() + "\n"
                       + QObject::tr("Location: ") + info.systemLocation() + "\n"
                       + QObject::tr("Description: ") + info.description() + "\n"
                       + QObject::tr("Manufacturer: ") + info.manufacturer() + "\n"
                       + QObject::tr("Serial number: ") + info.serialNumber() + "\n"
                       + QObject::tr("Vendor Identifier: ") + (info.hasVendorIdentifier() ? QString::number(info.vendorIdentifier(), 16) : QString()) + "\n"
                       + QObject::tr("Product Identifier: ") + (info.hasProductIdentifier() ? QString::number(info.productIdentifier(), 16) : QString()) + "\n\n\n";

         s = s + s1;
    }

    return s;
}

QStringList SerialManager::getPortsNames()
{
    getAllPortsInfo();
    return portsNames;
}
