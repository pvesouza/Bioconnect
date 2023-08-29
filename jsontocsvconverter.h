#ifndef JSONTOCSVCONVERTER_H
#define JSONTOCSVCONVERTER_H

#include <QObject>
#include <QFile>
#include <QTextStream>
#include <QDebug>
#include <cstdio>
#include <QDateTime>
#include <QJsonDocument>
#include <QJsonArray>
#include <QJsonObject>

class JsonToCsvConverter
{
public:
    JsonToCsvConverter();
    QString convertJsonToCSV(QString stringJson);
    void writecsv();
    void addLine(QString line);
    QJsonArray *getMeasurements();
    QByteArray convertToByteArray(const QJsonArray *myJson);
    void clearMeasurements();


private:
    const char *FILE_PATH = "C:\\Users\\pveso\\Documents\\Covid_Firmware\\data_curves\\";
    const char *fileName = "data";
    QString allLines = "";
    QJsonArray *measurements;

};

#endif // JSONTOCSVCONVERTER_H
