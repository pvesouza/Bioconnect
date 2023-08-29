#ifndef PROTOCOL_H
#define PROTOCOL_H

#include <QByteArray>
#include <QDebug>
#include <QHash>


class Protocol
{

public:

    Protocol();
    const char *CYCLIC = "scan\n";
    const char* CMD_VERSION_STRING = "pico_ver\n";

    typedef enum application_status{
        Emstat_OK,
        Emstat_NOK,
        Finished_Measurement,
        Begin_Measurement

    }STATUS;


private:

};

#endif // PROTOCOL_H
