#include "Envelope.h"
#include <string>
#include <map>
#include <vector>

using namespace std;


Envelope::Envelope()
{

}


string Envelope::getHeader( string key )
{
	return headers[key];
}

void Envelope::setHeader( string key, string value )
{
	headers[key] = value;
}

void Envelope::setPayload( vector<unsigned char> payload )
{
	this->payload = payload;
}

void Envelope::setHeaders( map<string, string> headers ) 
{
	this->headers = headers;
}
