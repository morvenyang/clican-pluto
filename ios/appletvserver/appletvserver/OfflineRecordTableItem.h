//
//  OfflineRecordTableItem.h
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import <Three20UI/Three20UI.h>

@interface OfflineRecordTableItem : TTTableStyledTextItem{
    TTButton* _deleteButton;
    TTButton* _actionButton;
}
@property (nonatomic, retain) TTButton* deleteButton;
@property (nonatomic, retain) TTButton* actionButton;
@end
