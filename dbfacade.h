#ifndef DBFACADE_H
#define DBFACADE_H

#include <QObject>
#include "dbcontroller.h"

class DbFacade : public QObject
{
    Q_OBJECT
public:
    explicit DbFacade(QObject *parent = nullptr);
    bool open();
    void close();
    void insert_values(const QString &filename, const QString &label, const bool vpd);

private:
    DbController *controller = nullptr;

signals:
    void db_error(const QString &error_message);

};

#endif // DBFACADE_H
