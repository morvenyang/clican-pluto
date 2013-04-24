//
//  IndexMenu.m
//  appletvserver
//
//  Created by zhang wei on 13-4-16.
//
//

#import "IndexMenu.h"

@implementation IndexMenu
@synthesize title = _title;
@synthesize onPlay = _onPlay;

-(void)dealloc{
    TT_RELEASE_SAFELY(_title);
    TT_RELEASE_SAFELY(_onPlay);
    [super dealloc];
}
@end
