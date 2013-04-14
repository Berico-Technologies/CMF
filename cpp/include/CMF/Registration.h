#ifndef REGISTRATION_H
#define REGISTRATION_H


class Registration
{
public:
	virtual void getRegistrationInfo() = 0;
	virtual void handle() = 0;
	virtual void handleFailed() = 0 ;
	
};

#endif
