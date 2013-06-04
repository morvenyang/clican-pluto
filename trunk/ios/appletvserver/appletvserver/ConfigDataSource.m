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
+ (ConfigDataSource*)dataSourceWithArrays:(NSArray*)array{
    NSMutableArray* items = [NSMutableArray array];
    NSMutableArray* sections = [NSMutableArray array];
    NSMutableArray* section = nil;
    for(id object in array){
        if ([object isKindOfClass:[NSString class]] ||
            [object isKindOfClass:[TTTableSection class]]) {
            [sections addObject:object];
            section = [NSMutableArray array];
            [items addObject:section];
            
        } else {
            [section addObject:object];
        }
        
    }
    
    return [[[self alloc] initWithItems:items sections:sections] autorelease];
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
