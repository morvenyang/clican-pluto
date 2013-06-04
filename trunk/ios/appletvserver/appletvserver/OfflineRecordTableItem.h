//
//  OfflineRecordTableItem.h
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import <Three20UI/Three20UI.h>
#import "TargetButton.h"
@interface OfflineRecordTableItem : TTTableStyledTextItem{
    TargetButton* _deleteButton;
    TargetButton* _actionButton;
}
@property (nonatomic, retain) TargetButton* deleteButton;
@property (nonatomic, retain) TargetButton* actionButton;
@end
