#include "Envelope.h"
#include "gtest/gtest.h"

namespace CMF {

// The fixture for testing class Envelope.
class EnvelopeTest : public ::testing::Test {
 protected:

  EnvelopeTest() {
    // You can do set-up work for each test here.
  }

  virtual ~EnvelopeTest() {
    // You can do clean-up work that doesn't throw exceptions here.
  }

  // If the constructor and destructor are not enough for setting up
  // and cleaning up each test, you can define the following methods:

  virtual void SetUp() {

  }

  virtual void TearDown() {

  }

};

// Tests that Foo does Xyz.
TEST_F(EnvelopeTest, 0is0) {
    ASSERT_TRUE(0 == 0 );
}

}  // namespace

int main(int argc, char **argv) {
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}
