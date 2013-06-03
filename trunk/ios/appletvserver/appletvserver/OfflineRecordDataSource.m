//
//  OfflineRecordDataSource.m
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import "OfflineRecordDataSource.h"
#import "OfflineRecordTableItem.h"
#import "OfflineRecordTableItemCell.h"
@implementation OfflineRecordDataSource

- (Class)tableView:(UITableView*)tableView cellClassForObject:(id) object {
    if ([object isKindOfClass:[OfflineRecordTableItem class]]) {
		return [OfflineRecordTableItemCell class];
	} else {
		return [super tableView:tableView cellClassForObject:object];
	}
}
@end
