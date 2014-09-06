//
//  KpiModel.h
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "Kpi.h"
@protocol KpiDelegate;

@interface KpiModel : TTURLRequestModel{
    id<KpiDelegate> _delegate;
}
@property(nonatomic,assign) id<KpiDelegate> delegate;
- (void)loadKpiByProjectId:(NSNumber*) projectId;
@end

@protocol KpiDelegate <NSObject>

- (void)loadKpiStart;
- (void)loadKpiSuccess:(NSDictionary*) kpis;
- (void)loadKpiFailed:(NSError*) error message:(NSString*) message;

@end
