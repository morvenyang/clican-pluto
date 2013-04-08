//
//  SMBDataSource.m
//  appletvserver
//
//  Created by zhang wei on 13-4-8.
//
//

#import "SMBDataSource.h"

@implementation SMBDataSource

@synthesize url = _url;

-(id) initWithUrl:(NSString*) url{
    self = [self init];
    if(self){
        self.url = url;
    }
    return self;
}
-(void) dealloc{
    TT_RELEASE_SAFELY(_url);
    [super dealloc];
}

- (void)load:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more {
    NSLog(@"call smb datasource callback");
    
}
@end
