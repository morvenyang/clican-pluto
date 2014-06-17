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
        
        NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
        formatter.numberStyle = NSNumberFormatterDecimalStyle;
        
        NSString* text = [formatter stringFromNumber:[NSNumber numberWithInt:brand.dayAmount.intValue/10000]];
        
        TTStyledText* styledText = [TTStyledText textFromXHTML:text lineBreaks:YES URLs:YES];
        NSString* url = [NSString stringWithFormat:@"peacebird://brand/%@", [brand.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];

        NSString* imageName = [NSString stringWithFormat:@"首页%@.png",brand.brand];
        [items addObject:[IndexTableItem itemWithStyledText:styledText backgroundImage:imageName URL:url]];
    }
    
    if ([items count] == 0) {
        [items addObject:[TTTableTextItem itemWithText:@"没有可查看的品牌数据"]];
    }
    
    
    self.items = items;
    NSLog(@"count=%i",[self.items count]);
    if(!_indexListModel.yesterday){
        NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
        [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
        TTAlert([NSString stringWithFormat:@"昨日数据未生成,当前数据为%@数据",[dateFormatter stringFromDate:_indexListModel.date]]);
    }
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
    return @"";
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
