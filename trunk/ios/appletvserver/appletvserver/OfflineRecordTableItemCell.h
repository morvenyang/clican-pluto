//
//  OfflineRecordTableItemCell.h
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import <Three20UI/Three20UI.h>
#import "OfflineRecordTableItem.h"
@interface OfflineRecordTableItemCell : TTStyledTextTableItemCell{
    OfflineRecordTableItem* _offlineRecordTableItem;
}
@property (nonatomic, retain) OfflineRecordTableItem* offlineRecordTableItem;
@end
