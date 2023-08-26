#ifndef JSONTOCSVCONVERTER_H
#define JSONTOCSVCONVERTER_H

#include <QObject>
#include <QFile>
#include <QTextStream>
#include <QDebug>
#include <cstdio>
#include <QDateTime>

class JsonToCsvConverter
{
public:
    JsonToCsvConverter();
    QString convertJsonToCSV(QString stringJson);
//    bool openCSVFile(int number);
    void writecsv();
//    bool isCSVOpened();
//    void closeCsv();
    void addLine(QString line);


private:
    const char *FILE_PATH = "C:\\Users\\pveso\\Documents\\Covid_Firmware\\data_curves\\";
    const char *fileName = "data";
    QString allLines = "";
//    bool isOpen = false;
//    QFile myfile;
//    QTextStream myStream;

};

#endif // JSONTOCSVCONVERTER_H
