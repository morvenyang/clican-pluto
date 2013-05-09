//
//  TTTabStripAddition.m
//  appletvserver
//
//  Created by zhang wei on 13-5-9.
//
//

#import "TTTabStripAddition.h"

@implementation TTTabStrip (SelectOffset)

- (void)selectTabIndex:(NSInteger)tabIndex withOffset:(float)offset {
    self.selectedTabIndex = tabIndex;
    _scrollView.contentOffset = CGPointMake(offset, 0);
    [self updateOverflow];
}

@end
