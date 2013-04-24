//
//  ListScrollerSplit.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "AtvListScrollerSplit.h"

@implementation AtvListScrollerSplit

@synthesize header = _header;
@synthesize items = _items;
- (void) dealloc {
    TT_RELEASE_SAFELY(_header);
    TT_RELEASE_SAFELY(_items);
    [super dealloc];
}

@end
