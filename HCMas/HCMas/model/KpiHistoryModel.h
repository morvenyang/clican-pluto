//
//  KpiHistoryModel.h
//  HCMas
//
//  Created by zhang wei on 14-9-10.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <Three20Network/Three20Network.h>
@protocol KpiHistoryDelegate;
@interface KpiHistoryModel : TTURLRequestModel{
    id<KpiHistoryDelegate> _delegate;
}
@property(nonatomic,assign) id<KpiHistoryDelegate> delegate;
- (void)loadHistoryKpiByProjectId:(NSNumber*) projectId kpiType:(NSString*)kpiType pointName:(NSString*) pointName startDate:(NSDate*)startDate endDate:(NSDate*)endDate;
@end

@protocol KpiHistoryDelegate <NSObject>

- (void)loadKpiHistoryStart;
- (void)loadKpiHistorySuccess:(NSArray*) kpis;
- (void)loadKpiHistoryFailed:(NSError*) error message:(NSString*) message;

@end