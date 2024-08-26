#ifndef SERIALMANAGER_H
#define SERIALMANAGER_H

#include <QObject>
#include <QWidget>
#include <QtSerialPort/QSerialPortInfo>

class SerialManager : QObject
{
    Q_OBJECT
public:
    SerialManager();                            // Constructor
    ~SerialManager();                           // Destructor

    int serialCount(void);                      // Returns the number of available serials
    QList<QSerialPortInfo> getAll(void);        // Get all availables serial ports
    QString getAllPortsInfo(void);              // Returns a string containing all ports data
    QStringList getPortsNames(void);
//    QSerialPortInfo *getPortInfo(QString name);

private:
    QList<QSerialPortInfo> myPortList;
    QSerialPortInfo *myPortInfo;
    QStringList portsNames;

};

#endif // SERIALMANAGER_H
