//
//  AtvOneLineMenuItem.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "AtvOneLineMenuItem.h"

@implementation AtvOneLineMenuItem

@synthesize label = _label;
@synthesize onPlay = _onPlay;
@synthesize onSelect = _onSelect;
@synthesize image = _image;

- (void) dealloc {
    TT_RELEASE_SAFELY(_label);
    TT_RELEASE_SAFELY(_onPlay);
    TT_RELEASE_SAFELY(_onSelect);
    TT_RELEASE_SAFELY(_image);
    [super dealloc];
}
@end
