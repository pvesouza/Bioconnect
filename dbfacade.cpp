#include "dbfacade.h"


DbFacade::DbFacade(QObject *parent)
    : QObject{parent}
{
    this->controller = new DbController();
}

bool DbFacade::open()
{
    return this->controller->open();
}

void DbFacade::close()
{
    return this->controller->close();
}

void DbFacade::insert_values(const QString &filename, const QString &label, const bool vpd)
{
    bool result = true;
    if (vpd)
    {
        result = this->controller->insert_pdv(filename, label);
    }else{
        result = this->controller->insert_cyclic(filename, label);
    }

    // Error Handling
    if (!result)
    {
        QString message = this->controller->getError_message();
        emit db_error(message);

    }
}
