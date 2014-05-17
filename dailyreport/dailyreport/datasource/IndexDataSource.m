//
//  IndexDataSource.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexDataSource.h"
#import "Brand.h"

@implementation IndexDataSource
@synthesize indexListModel=_indexListModel;
- (id)init{
    if ((self = [super init])) {
        self.indexListModel = [[[IndexListModel alloc] init] autorelease];
    }
    return self;
}

- (id<TTModel>)model {
    return _indexListModel;
}

- (void)tableViewDidLoadModel:(UITableView*)tableView {
    NSMutableArray* items = [[NSMutableArray alloc] init];
    
    for (Brand* brand in _indexListModel.brandList) {
        TTStyledText* styledText = [TTStyledText textFromXHTML:[brand.brand stringByAppendingFormat:@" %@ 万元",brand.dayAmount] lineBreaks:YES URLs:YES];
        NSString* url = [NSString stringWithFormat:@"peacebird://retail/%@", brand.brand];
        [items addObject:[TTTableStyledTextItem itemWithText:styledText URL:url]];
    }
    
    if ([items count] == 0) {
        [items addObject:[TTTableTextItem itemWithText:@"没有可查看的品牌数据"]];
    }
    
    
    self.items = items;
    NSLog(@"count=%i",[self.items count]);
    TT_RELEASE_SAFELY(items);
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)titleForLoading:(BOOL)reloading {
    return @"更新";
}

///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)titleForEmpty {
    return @"没有可查看的品牌数据";
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)subtitleForError:(NSError*)error {
    return @"加载品牌数据失败";
}
@end
