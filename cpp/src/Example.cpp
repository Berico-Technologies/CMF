#include <iostream>
#include <SimpleAmqpClient/SimpleAmqpClient.h>
#include <string>
#include <unistd.h>
using namespace std;
using namespace AmqpClient;

int main()
{
	Channel::ptr_t channel = Channel::Create("192.168.100.226");
	channel->DeclareQueue("travis_q");
    channel->BindQueue("travis_q", "amq.direct", "tckey");
  
   	BasicMessage::ptr_t msg_in = BasicMessage::Create();

    msg_in->Body("This TC talking to rabbit.");

    channel->BasicPublish("amq.direct", "tckey", msg_in);

    string tag = channel->BasicConsume("travis_q", "consumertag");
    Envelope::ptr_t envelope = channel->BasicConsumeMessage(tag);
  	BasicMessage::ptr_t msg_out = envelope->Message();
  	cout << msg_out->Body() << endl;

  	//do a sleep for 20 secs so I can see the queue in Rabbit
  	usleep(20000000);

	return 0;
}
