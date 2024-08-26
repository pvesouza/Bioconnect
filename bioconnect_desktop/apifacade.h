#ifndef APIFACADE_H
#define APIFACADE_H

#include <QObject>
#include <QCoreApplication>
#include <QNetworkAccessManager>
#include <QNetworkRequest>
#include <QNetworkReply>

#include <QJsonObject>
#include <QJsonDocument>
#include <QJsonArray>


class ApiFacade : public QObject
{
     Q_OBJECT

public:
    ApiFacade(QObject *parent = nullptr);
    void sendMeasurements(const QByteArray &measurements);
    QNetworkReply *reply;

signals:
    void on_network_response(QString response);


private:
    const char *ENDPOINT_ROOT = "http://127.0.0.0:5000";
    const char *PREDICT = "/predict";

private slots:
        void handle_network_reply();
};

#endif // APIFACADE_H
