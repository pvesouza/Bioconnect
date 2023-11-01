#include "dbcontroller.h"

DbController::DbController(QObject *parent)
    : QObject{parent}
{
    database = new QSqlDatabase(QSqlDatabase::addDatabase("QPSQL"));
    if (database != nullptr)
    {
        database->setHostName(this->HOSTNAME);
        database->setDatabaseName(this->DATABASE_NAME);
        database->setUserName(this->USERNAME);
        database->setPassword(this->PASSWORD);
        database->setPort(5432);
        this->my_db = *database;

    }else{
        qDebug() << "Error to connect with database server";
    }

}

bool DbController::open()
{
   bool result = false;
   if (database == nullptr)
   {
       return result;
   }
   if (!this->database->isOpen()){
       result = this->database->open();
   }else{
       result = true;
   }
   return result;
}

void DbController::close()
{
    if (this->database->isOpen())
    {
        this->database->close();
    }
}

bool DbController::insert_pdv(const QJsonArray *data, const QString &label)
{
    bool result = false;
    QJsonDocument doc;
    doc.setArray(*data);
    QString dataToString(doc.toJson());
    qDebug() << "Cyclic: " << dataToString;

//    const QString query_prepare = "INSERT INTO teste(name) VALUES (:json_data)";
    const QString query_prepare = "INSERT INTO cyclic(json_data, text_label) VALUES (:json_data, :text_label)";
    QSqlQuery my_query(this->my_db);
    my_query.prepare(query_prepare);
    my_query.bindValue(":json_data", dataToString);
    my_query.bindValue(":text_label", label);

    if (this->open())
    {
        result = my_query.exec();
    }
    this->close();
    if (!result)
    {
        this->setError_message(my_query.lastError().text());
    }
    return result;
}

bool DbController::insert_cyclic(const QJsonArray *data, const QString &label)
{
    bool result = false;
    QJsonDocument doc;
    doc.setArray(*data);
    QString dataToString(doc.toJson());
    qDebug() << "Cyclic: " << dataToString;

//    const QString query_prepare = "INSERT INTO teste(name) VALUES (:json_data)";
    const QString query_prepare = "INSERT INTO cyclic(json_data, text_label) VALUES (:json_data, :text_label)";
    QSqlQuery my_query(this->my_db);
    my_query.prepare(query_prepare);
    my_query.bindValue(":json_data", dataToString);
    my_query.bindValue(":text_label", label);

    if (this->open())
    {
        result = my_query.exec();
    }
    this->close();
    if (!result)
    {
        this->setError_message(my_query.lastError().text());
    }
    return result;
}

const QString &DbController::getError_message() const
{
    return error_message;
}

void DbController::setError_message(const QString &newError_message)
{
    error_message = newError_message;
}
