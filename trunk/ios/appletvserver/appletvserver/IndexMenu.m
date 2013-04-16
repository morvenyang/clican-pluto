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
@synthesize onSelect = _onSelect;

-(void)dealloc{
    TT_RELEASE_SAFELY(_title);
    TT_RELEASE_SAFELY(_onSelect);
    [super dealloc];
}
@end
