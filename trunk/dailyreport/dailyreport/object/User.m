

#import "User.h"


@implementation User

@synthesize username   = _username;
@synthesize password   = _password;
@synthesize sessionId   = _sessionId;
@synthesize expiredDays = _expiredDays;
@synthesize date = _date;
@synthesize oldDate = _oldDate;
@synthesize timeoutInterval = _timeoutInterval;
@synthesize showGestureLock = _showGestureLock;
@synthesize goods = _goods;
@synthesize goodIndex =_goodIndex;
///////////////////////////////////////////////////////////////////////////////////////////////////
-(id)init{
    self = [super init];
    if(self){
        self.timeoutInterval = 60;
        return self;
    }
    return nil;
}
- (void) dealloc {
    TT_RELEASE_SAFELY(_username);
    TT_RELEASE_SAFELY(_password);
    TT_RELEASE_SAFELY(_sessionId);
    TT_RELEASE_SAFELY(_expiredDays);
    TT_RELEASE_SAFELY(_date);
    TT_RELEASE_SAFELY(_oldDate);
    TT_RELEASE_SAFELY(_goods);
    TT_RELEASE_SAFELY(_goodDate);
    [super dealloc];
}


@end
