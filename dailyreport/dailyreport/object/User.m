

#import "User.h"


@implementation User

@synthesize username   = _username;
@synthesize password   = _password;
@synthesize sessionId   = _sessionId;
@synthesize expiredDays = _expiredDays;
///////////////////////////////////////////////////////////////////////////////////////////////////
- (void) dealloc {
    TT_RELEASE_SAFELY(_username);
    TT_RELEASE_SAFELY(_password);
    TT_RELEASE_SAFELY(_sessionId);
    TT_RELEASE_SAFELY(_expiredDays);
    [super dealloc];
}


@end
