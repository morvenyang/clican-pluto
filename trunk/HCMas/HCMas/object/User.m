

#import "User.h"


@implementation User

@synthesize username   = _username;
@synthesize password   = _password;
@synthesize sessionId   = _sessionId;
@synthesize selectedProject = _selectedProject;

-(id)init{
    self = [super init];
    if(self){
        return self;
    }
    return nil;
}
- (void) dealloc {
    TT_RELEASE_SAFELY(_username);
    TT_RELEASE_SAFELY(_password);
    TT_RELEASE_SAFELY(_sessionId);
    TT_RELEASE_SAFELY(_selectedProject);
    [super dealloc];
}


@end
