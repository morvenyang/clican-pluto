//
//  ConfigDataSource.h
//  appletvserver
//
//  Created by zhang wei on 13-4-1.
//
//

#import <Three20UI/Three20UI.h>
@protocol ConfigCallback
-(void) updateConfig;
@end

@class ConfigCallback;

@interface ConfigDataSource : TTListDataSource{
    TTTableControlItem* _clearCacheItem;
    ConfigCallback* _callback;
}
@property (nonatomic, retain) TTTableControlItem    *clearCacheItem;
@property (nonatomic, retain) ConfigCallback    *callback;
-(id) initWithItems:(NSArray *)items callback:(id) callback;
@end
