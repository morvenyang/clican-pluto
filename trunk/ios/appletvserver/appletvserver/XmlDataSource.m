//
//  XmlDataSource.m
//  appletvserver
//
//  Created by zhang wei on 13-3-26.
//
//

#import "XmlDataSource.h"
#import "Video.h"
#import "VideoTableItem.h"
#import "VideoTableItemCell.h"

@implementation XmlDataSource

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
@end
