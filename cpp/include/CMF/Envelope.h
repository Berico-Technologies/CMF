#ifndef ENEVLOPE_H
#define ENEVLOPE_H

#include <map>
#include <vector>
#include <string>


class Envelope
{
	public:
		Envelope();
		
		~Envelope();
		
        std::map<std::string, std::string> getHeaders() { return headers; }
		
        std::vector<unsigned char> getPayload() { return payload; }
		
		std::string getHeader(const std::string key);
		
		void setHeader(const std::string key, const std::string value);
		
		void setHeaders( std::map<std::string, std::string> headers);
		
		void setPayload(std::vector<unsigned char> payload );


	protected:
		std::map<std::string, std::string> headers;
		
		std::vector<unsigned char> payload;
	/* data */
};


#endif
