#include "dbcontroller.h"

DbController::DbController(QObject *parent)
    : QObject{parent}
{
    my_db = QSqlDatabase::addDatabase(this->DATABASE_NAME);
    database = &my_db;
}

bool DbController::open()
{
   bool result = false;
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

bool DbController::insert_pdv(const QString &filename, const QString &label)
{
    bool result = false;
    const QString query_prepare = "INSERT INTO vpd (filename, label) VALUES (:filename, :label)";
    QSqlQuery my_query;
    my_query.prepare(query_prepare);
    my_query.bindValue(":filename", filename);
    my_query.bindValue(":label", label);

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

bool DbController::insert_cyclic(const QString &filename, const QString &label)
{
    bool result = false;
    const QString query_prepare = "INSERT INTO cyclic (filename, label) VALUES (:filename, :label)";
    QSqlQuery my_query;
    my_query.prepare(query_prepare);
    my_query.bindValue(":filename", filename);
    my_query.bindValue(":label", label);

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
