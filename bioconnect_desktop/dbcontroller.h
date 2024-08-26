#ifndef DBCONTROLLER_H
#define DBCONTROLLER_H

#include <QObject>
#include <QSqlDatabase>
#include <QSqlQuery>
#include <QtSql>
#include <QDebug>



class DbController : public QObject
{
    Q_OBJECT
public:
    explicit DbController(QObject *parent = nullptr);
    bool open();
    void close();
    bool insert_pdv(const QJsonArray *data, const QString &label);
    bool insert_cyclic(const QJsonArray *data, const QString &label);


    const QString &getError_message() const;
    void setError_message(const QString &newError_message);

private:
    const QString DATABASE_NAME = "postgres";
    const QString HOSTNAME = "localhost";
    const QString USERNAME = "postgres";
    const QString PASSWORD = "admin";
    QSqlDatabase* database = nullptr;
    QSqlDatabase my_db;
    QString error_message;



signals:

};

#endif // DBCONTROLLER_H
