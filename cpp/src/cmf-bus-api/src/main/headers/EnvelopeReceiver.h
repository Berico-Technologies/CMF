#ifndef ENVELOPERECEIVER_H
#define ENVELOPERECEIVER_H

class EnvelopeReceiver
{
public:
	virtual void reg() = 0;
	virtual void unreg() = 0;
};

#endif // ENVELOPERECEIVER_H
