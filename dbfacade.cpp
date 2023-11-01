#include "dbfacade.h"


DbFacade::DbFacade(QObject *parent)
    : QObject{parent}
{
    this->controller = new DbController();
//    if (this->controller->open()){
//        emit db_error("Db Connected Sucessfully");
//    }else{
//        emit db_error("DB not connected");
//    }
}

bool DbFacade::open()
{
    return this->controller->open();
}

void DbFacade::close()
{
    return this->controller->close();
}

void DbFacade::insert_values(const QJsonArray *data, const QString &label, const bool vpd)
{
    bool result = true;
    if (vpd)
    {
        result = this->controller->insert_pdv(data, label);
    }else{
        result = this->controller->insert_cyclic(data, label);
    }

    // Error Handling
    if (!result)
    {
        QString message = this->controller->getError_message();
        emit db_error(message);
    }else{
        emit db_error("Success");
    }
}
