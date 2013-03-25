//
//  AtvHeader.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "AtvHeader.h"

@implementation AtvHeader

@synthesize title = _title;

- (void) dealloc {
    TT_RELEASE_SAFELY(_title);
    [super dealloc];
}
@end
