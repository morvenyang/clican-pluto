//
//  ConfigDataSource.m
//  appletvserver
//
//  Created by zhang wei on 13-4-1.
//
//

#import "ConfigDataSource.h"
#import "Constants.h"

@implementation ConfigDataSource
@synthesize callback = _callback;
-(id) initWithItems:(NSArray *)items callback:(id) callback{
    self = [super initWithItems:items];
    if(self){
        self.callback = callback;
    }
    return self;
}
- (void)load:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more {
    NSLog(@"call config callback");
    [self.callback updateConfig];
}

-(void) dealloc{
    TT_RELEASE_SAFELY(_callback);
    [super dealloc];
}
@end
