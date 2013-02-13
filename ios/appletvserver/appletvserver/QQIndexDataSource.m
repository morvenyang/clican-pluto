//
//  QQIndexDataSource.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQIndexDataSource.h"
#import "Video.h"
#import "VideoTableItem.h"
#import "VideoTableItemCell.h"
@implementation QQIndexDataSource

@synthesize qqIndexRequestModel=_qqIndexRequestModel;

- (id)initWithQQChannel:(QQChannel)channel{
    if ((self = [super init])) {
        self.qqIndexRequestModel = [[QQIndexRequestModel alloc] initWithQQChannel:channel keyword:nil searchAlbum:false];
    }
    return self;
}

- (void)dealloc {
    [super dealloc];
    TT_RELEASE_SAFELY(_qqIndexRequestModel);
}

- (id<TTModel>)model {
    return _qqIndexRequestModel;
}

- (void)tableViewDidLoadModel:(UITableView*)tableView {
    NSMutableArray* items = [[NSMutableArray alloc] init];
    NSLog(@"video account in videoList= %i",[_qqIndexRequestModel.videoList count]);
    for(int i=0;i<[_qqIndexRequestModel.videoList count];i=i+3){
        NSMutableArray* cellItems = [NSMutableArray array];
        for(int j=i;j<i+3&&j<[_qqIndexRequestModel.videoList count];j++){
            Video* video = [_qqIndexRequestModel.videoList objectAtIndex:j];
            [cellItems addObject:video];
        }
        [items addObject:[VideoTableItem itemWithVideoList:cellItems]];
    }
    
    if (!_qqIndexRequestModel.finished) {
        TTTableMoreButton* moreButton = [TTTableMoreButton itemWithText:@"更多"];
        [items addObject:moreButton];
    }
    
    if ([items count] == 0) {
        [items addObject:[TTTableTextItem itemWithText:@"没有相关视频" ]];
    }
    
    
    self.items = items;
    NSLog(@"count=%i",[self.items count]);
    TT_RELEASE_SAFELY(items);
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)titleForLoading:(BOOL)reloading {
    return @"加载中...";
}

- (Class)tableView:(UITableView*)tableView cellClassForObject:(id) object { 
    if ([object isKindOfClass:[VideoTableItem class]]) { 
		return [VideoTableItemCell class];  		
	}else if([object isKindOfClass:[TTTableMoreButton class]]){
        return [TTTableMoreButtonCell class];
    }else if([object isKindOfClass:[TTTableTextItem class]]){
        return [TTTableTextItemCell class];
    }  else { 
		return [super tableView:tableView cellClassForObject:object]; 
	}
	
}

///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)titleForEmpty {
    return @"无相关视频";
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)subtitleForError:(NSError*)error {
    return @"系统错误";
}
@end
