#include "jsontocsvconverter.h"

JsonToCsvConverter::JsonToCsvConverter(QObject *parent)
    : QObject{parent}

{
    measurements = new QJsonArray();
}

QString JsonToCsvConverter::convertJsonToCSV(QString stringJson)
{
    // Split into more strings
    QStringList measures = stringJson.split(",");
    QString potential, current, result;

    for (int i = 0; i < measures.length(); i++)
    {
        QString s = measures.at(i);
        //Pegando o valor da tensão
        if (s.contains("\"V\":"))
        {
            bool read = false;
            for (int j = 0; j < s.length(); j++)
            {
                QChar c = s.at(j);
                if (c == ':'){
                    read = true;
                }
                else if (read)
                {
                    potential = potential.append(c);
                }
            }
        }
        // Pega o valor da corrente
        else if (s.contains("\"I\":"))
        {
            bool read = false;
            for (int j = 0; j < s.length() - 1; j++)
            {
                QChar c = s.at(j);
                if (c == ':'){
                    read = true;
                }
                else if (read)
                {
                    current = current.append(c);
                }
            }
        }
    }

    if (potential.length() > 2 && current.length() > 2)
    {
        result = result.append(potential);
        result = result.append(",");
        result = result.append(current);
        result = result.append('\n');
//        qDebug() << "potential: " << potential;
//        qDebug() << "current: " << current;
    }

    return result;
}


//bool JsonToCsvConverter::openCSVFile(int number)
//{
//    char filename[100];
//    sprintf(filename, "%s%s_%d.csv", FILE_PATH, fileName, number);
//    qDebug() << filename;
//    myfile.setFileName(fileName);
//    isOpen = myfile.open(QIODevice::WriteOnly | QIODevice::Text);
//    allLines.clear();
//    return isOpen;
//}

void JsonToCsvConverter::writecsv(const char *filePath)
{

    char filename[100];
    QString date = QString::number(QDateTime::currentMSecsSinceEpoch());
    if (filePath != nullptr)
    {
        sprintf(filename, "%s/%s_%s.csv", filePath, fileName, date.toStdString().c_str());
        qDebug() << filename;
        QFile myFile(filename);
        bool isOpen  = myFile.open(QIODevice::WriteOnly | QIODevice::Text);
        if (isOpen)
        {
            QTextStream stream(&myFile);
            stream << "potential,current\n";
            stream << allLines;
            //qDebug() << "file written";
            myFile.flush();
            myFile.close();
            allLines.clear();
        }else{
            emit fileNotSavedError();
        }
    }else{
        emit fileNotSavedError();
    }
}

void JsonToCsvConverter::addLine(QString line)
{
    QStringList numbers = line.split(",");

    double potential = numbers[0].toDouble();
    double current = numbers[1].toDouble();

    QString potential_value = QString::number(potential, 'f', std::numeric_limits<double>::max_digits10);
    QString current_value = QString::number(current, 'f', std::numeric_limits<double>::max_digits10);

    allLines = allLines.append(potential_value);
    allLines.append(',');
    allLines.append(current_value);
    allLines.append('\n');

    QJsonObject json;
    json["V"] = potential_value;
    json["I"] = current_value;
    measurements->append(json);
}

QByteArray JsonToCsvConverter::getMeasurements()
{
    QByteArray byteData = convertToByteArray(measurements);
    return byteData;
}

QByteArray JsonToCsvConverter::convertToByteArray(const QJsonArray *myJson)
{
    // Convert JSON data to QByteArray
    qDebug() << "Jsonsize(): " << myJson->size();
    QJsonDocument jsonDoc(*myJson);
    QByteArray postData = jsonDoc.toJson();
    return postData;
}

void JsonToCsvConverter::saveJsonFile(const char *filePath)
{
    char filename[100];
    QString date = QString::number(QDateTime::currentMSecsSinceEpoch());
    if (filePath != nullptr)
    {
        sprintf(filename, "%s/%s_%s_json.csv", filePath, fileName, date.toStdString().c_str());
        QFile myFile(filename);
        bool isOpen  = myFile.open(QIODevice::WriteOnly | QIODevice::Text);
        if (isOpen)
        {
            QTextStream stream(&myFile);
            stream << this->convertToByteArray(measurements).constData();
            myFile.flush();
            myFile.close();
        }else{
            emit fileNotSavedError();
        }
    }else{
        emit fileNotSavedError();
    }
}

void JsonToCsvConverter::clearMeasurements()
{
    for (int i = 0; i < measurements->size(); i++)
    {
        measurements->removeAt(i);
    }
}
