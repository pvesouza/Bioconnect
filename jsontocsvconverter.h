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

class JsonToCsvConverter : public QObject
{
    Q_OBJECT;

public:
    JsonToCsvConverter(QObject *parent = nullptr);
    QString convertJsonToCSV(QString stringJson);
    void writecsv(const char *filePath);
    void addLine(QString line);
    QByteArray getMeasurements();
    QByteArray convertToByteArray(const QJsonArray *myJson);
    void saveJsonFile(const char *filePath);
    void clearMeasurements();

signals:
    void fileNotSavedError(void);


private:
    const char *FILE_PATH = "C:\\Users\\pveso\\Documents\\Covid_Firmware\\data_curves\\";
    const char *fileName = "data";
    QString filePath;
    QString allLines = "";
    QJsonArray *measurements;

};

#endif // JSONTOCSVCONVERTER_H
