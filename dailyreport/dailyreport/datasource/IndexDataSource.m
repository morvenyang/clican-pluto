//
//  IndexDataSource.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexDataSource.h"
#import "Brand.h"
#import "IndexTableItem.h"
#import "IndexTableItemCell.h"

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
        TTStyledText* styledText = [TTStyledText textFromXHTML:[brand.brand stringByAppendingFormat:@"昨日零售收入 %@万元",brand.dayAmount] lineBreaks:YES URLs:YES];
        NSString* url = [NSString stringWithFormat:@"peacebird://brand/%@", brand.brand];
        NSString* imageName = [NSString stringWithFormat:@"首页%@.png",brand.brand];
        [items addObject:[IndexTableItem itemWithStyledText:styledText backgroundImage:imageName URL:url]];
    }
    
    if ([items count] == 0) {
        [items addObject:[TTTableTextItem itemWithText:@"没有可查看的品牌数据"]];
    }
    
    
    self.items = items;
    NSLog(@"count=%i",[self.items count]);
    TT_RELEASE_SAFELY(items);
}

- (Class)tableView:(UITableView*)tableView cellClassForObject:(id) object {
    if ([object isKindOfClass:[IndexTableItem class]]) {
		return [IndexTableItemCell class];
	} else {
		return [super tableView:tableView cellClassForObject:object];
	}
	
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
