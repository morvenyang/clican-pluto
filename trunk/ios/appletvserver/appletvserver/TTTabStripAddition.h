//
//  TTTabStripAddition.h
//  appletvserver
//
//  Created by zhang wei on 13-5-9.
//
//

#import <Foundation/Foundation.h>

@interface TTTabStrip (Private)
- (void)updateOverflow;
- (void)selectTabIndex:(NSInteger)tabIndex withOffset:(float)offset;
@end
